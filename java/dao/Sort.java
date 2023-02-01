package com.hv.data;

public class Sort {

    private String sortName;
    private String sortOrder;

    public Sort(String sortName, String sortOrder) {
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortString() {
        StringBuilder sb = new StringBuilder();
        String[] names = getSortName().split(",");
        String[] orders = getSortOrder().split(",");
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("`").append(names[i]).append("` ").append(orders[i]);
        }
        String ret = sb.toString();
        if (ret.length() > 0) {
            ret = " order by " + ret;
        }
        return ret;
    }
}
