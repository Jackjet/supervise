package gov.df.supervise.bean.view;

public class CsofCViewEntity {
    private String viewId;

    private String viewCode;

    private String viewName;

    private String objtypeId;

    private String objtypeCode;

    private String objtypeName;

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId == null ? null : viewId.trim();
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode == null ? null : viewCode.trim();
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName == null ? null : viewName.trim();
    }

    public String getObjtypeId() {
        return objtypeId;
    }

    public void setObjtypeId(String objtypeId) {
        this.objtypeId = objtypeId == null ? null : objtypeId.trim();
    }

    public String getObjtypeCode() {
        return objtypeCode;
    }

    public void setObjtypeCode(String objtypeCode) {
        this.objtypeCode = objtypeCode == null ? null : objtypeCode.trim();
    }

    public String getObjtypeName() {
        return objtypeName;
    }

    public void setObjtypeName(String objtypeName) {
        this.objtypeName = objtypeName == null ? null : objtypeName.trim();
    }
}