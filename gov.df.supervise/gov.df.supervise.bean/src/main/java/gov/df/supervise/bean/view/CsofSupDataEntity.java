package gov.df.supervise.bean.view;

public class CsofSupDataEntity {
    private String dataId;

    private String viewId;

    private String sheetId;

    private String dataTable;

    private String createDate;

    private String lastVer;

    private String billtypeCode;

    private String billId;

    private String objtypeId;

    private String objId;

    private Short supCycle;

    private String supDate;

    private String oid;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId == null ? null : dataId.trim();
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId == null ? null : viewId.trim();
    }

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable == null ? null : dataTable.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getLastVer() {
        return lastVer;
    }

    public void setLastVer(String lastVer) {
        this.lastVer = lastVer == null ? null : lastVer.trim();
    }

    public String getBilltypeCode() {
        return billtypeCode;
    }

    public void setBilltypeCode(String billtypeCode) {
        this.billtypeCode = billtypeCode == null ? null : billtypeCode.trim();
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId == null ? null : billId.trim();
    }

    public String getObjtypeId() {
        return objtypeId;
    }

    public void setObjtypeId(String objtypeId) {
        this.objtypeId = objtypeId == null ? null : objtypeId.trim();
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public Short getSupCycle() {
        return supCycle;
    }

    public void setSupCycle(Short supCycle) {
        this.supCycle = supCycle;
    }

    public String getSupDate() {
        return supDate;
    }

    public void setSupDate(String supDate) {
        this.supDate = supDate == null ? null : supDate.trim();
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }
}