package com.sanryoo.matcher.modal;

public class MatchData {

    private String ids = "";

    private Long idsend = 0L;

    private Integer data = 0;

    public MatchData() {
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getIdsend() {
        return idsend;
    }

    public void setIdsend(Long idsend) {
        this.idsend = idsend;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CancelMatchData{" +
                "ids='" + ids + '\'' +
                ", idsend=" + idsend +
                ", data='" + data + '\'' +
                '}';
    }
}
