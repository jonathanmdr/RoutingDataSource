package br.com.multidatasources.multidatasources.config.datasource;

public enum DataSourceType {

    READ_ONLY("Slave-DB") {
        @Override
        int getDefaultPoolSize() {
            return Runtime.getRuntime().availableProcessors() * 4;
        }
    },
    READ_WRITE("Master-DB") {
        @Override
        int getDefaultPoolSize() {
            return Runtime.getRuntime().availableProcessors() * 4;
        }
    };

    private final String poolName;

    DataSourceType(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolName() {
        return this.poolName;
    }

    abstract int getDefaultPoolSize();

}
