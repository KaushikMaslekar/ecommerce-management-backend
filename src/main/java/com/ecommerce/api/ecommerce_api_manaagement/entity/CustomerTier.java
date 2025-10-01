package com.ecommerce.api.ecommerce_api_manaagement.entity;

public enum CustomerTier {
    BRONZE("Bronze", 0),
    SILVER("Silver", 1000),
    GOLD("Gold", 5000),
    PLATINUM("Platinum", 10000);

    private final String displayName;
    private final int minimumPoints;

    CustomerTier(String displayName, int minimumPoints) {
        this.displayName = displayName;
        this.minimumPoints = minimumPoints;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinimumPoints() {
        return minimumPoints;
    }
}
