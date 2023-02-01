package com.hv.data;

import java.util.ArrayList;

public class Filter {

    private ArrayList<String> data;
    private ArrayList<Filter> and;
    private ArrayList<Filter> or;
    private String where;
    private ArrayList<Object> args;
    private boolean prepared = false;

    public Filter() {
        and = new ArrayList<>();
        or = new ArrayList<>();
    }

    public Filter(String key, String oper, String value) {
        data = new ArrayList<>();
        data.add(key);
        data.add(oper);
        data.add(value);
        and = new ArrayList<>();
        or = new ArrayList<>();
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<Filter> getAnd() {
        return and;
    }

    public void setAnd(ArrayList<Filter> and) {
        this.and = and;
    }

    public ArrayList<Filter> getOr() {
        return or;
    }

    public void setOr(ArrayList<Filter> or) {
        this.or = or;
    }

    public String getWhere() {
        if (!prepared) {
            prepare();
        }
        return where;
    }

    public ArrayList<Object> getArgs() {
        if (!prepared) {
            prepare();
        }
        return args;
    }

    private void prepare() {
        StringBuilder sb = new StringBuilder();
        args = new ArrayList<>();
        if (data != null && data.size() == 3) {
            if (data.get(1).contains("in")) {
                sb.append("(`").append(data.get(0)).append("` ").append(data.get(1)).append(" (");
                String[] values = data.get(2).split(",");
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append("?");
                    args.add(values[i]);
                }
                sb.append("))");
            } else {
                sb.append("(`").append(data.get(0)).append("` ").append(data.get(1)).append(" ?)");
                args.add(data.get(2));
            }
        }

        if (and != null && and.size() > 0) {
            if (data != null && data.size() == 3) {
                sb.append(" and ");
            }
            sb.append("(");
            for (int i = 0; i < and.size(); i++) {
                if (i > 0) {
                    sb.append(" and ");
                }
                sb.append(and.get(i).getWhere());
                args.addAll(and.get(i).getArgs());
            }
            sb.append(")");
        }

        if (or != null && or.size() > 0) {
            if ((data != null && data.size() == 3) || (and != null && and.size() > 0)) {
                sb.append(" and ");
            }
            sb.append("(");
            for (int i = 0; i < or.size(); i++) {
                if (i > 0) {
                    sb.append(" or ");
                }
                sb.append(or.get(i).getWhere());
                args.addAll(or.get(i).getArgs());
            }
            sb.append(")");
        }

        where = sb.toString();
        prepared = true;
    }
}
