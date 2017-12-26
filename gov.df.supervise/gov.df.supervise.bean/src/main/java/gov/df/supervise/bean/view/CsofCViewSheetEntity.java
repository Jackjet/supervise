package gov.df.supervise.bean.view;

public class CsofCViewSheetEntity {
    private String sheetId;

    private String viewId;

    private String sheetCode;

    private String sheetName;

    private Short sheetType;

    private String dataTable;

    private String createDate;

    private String lastVer;

    private Short isValid;

    private Short sheetIndex;

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId == null ? null : viewId.trim();
    }

    public String getSheetCode() {
        return sheetCode;
    }

    public void setSheetCode(String sheetCode) {
        this.sheetCode = sheetCode == null ? null : sheetCode.trim();
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName == null ? null : sheetName.trim();
    }

    public Short getSheetType() {
        return sheetType;
    }

    public void setSheetType(Short sheetType) {
        this.sheetType = sheetType;
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

    public Short getIsValid() {
        return isValid;
    }

    public void setIsValid(Short isValid) {
        this.isValid = isValid;
    }

    public Short getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Short sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
}