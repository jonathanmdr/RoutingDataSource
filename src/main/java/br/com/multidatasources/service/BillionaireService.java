package br.com.multidatasources.service;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.repository.BillionaireRepository;
import br.com.multidatasources.service.idempotency.IdempotencyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillionaireService {

    private final BillionaireRepository billionaireRepository;
    private final IdempotencyGenerator idempotencyGenerator;

    public BillionaireService(
        final BillionaireRepository billionairesRepository,
        final IdempotencyGenerator idempotencyGenerator
    ) {
        this.billionaireRepository = billionairesRepository;
        this.idempotencyGenerator = idempotencyGenerator;
    }

    @Transactional(readOnly = true)
    public Billionaire findById(final Long id) {
        return billionaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Register with id %s not found", id)));
    }

    @Transactional(readOnly = true)
    public List<Billionaire> findAll() {
        return billionaireRepository.findAll();
    }

    public Billionaire save(final Billionaire billionaire) {
        billionaire.generateIdempotencyId(idempotencyGenerator);

        final var exists = billionaireRepository.existsBillionaireByIdempotencyId(billionaire.getIdempotencyId());

        if (exists) {
            throw new EntityExistsException("Register has exists with idempotency ID: %s".formatted(billionaire.getIdempotencyId()));
        }

        return billionaireRepository.save(billionaire);
    }

    public void delete(final Billionaire billionaire) {
        billionaireRepository.delete(billionaire);
    }

}
