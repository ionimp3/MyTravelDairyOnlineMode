package DataView;

public class InfoTBmodelFragment {
    private String InfoTB_planDepartDate;
    private String InfoTB_planEndDate;
    private String InfoTB_planName;
    private String InfoTB_planTags;
    private String InfoTB_titleImage;
    private String InfoTB_planCountry;

    public InfoTBmodelFragment() {
    }

    @Override
    public String toString() {
        return "InfoTBmodelFragment{" +
                "InfoTB_planDepartDate='" + InfoTB_planDepartDate + '\'' +
                ", InfoTB_planEndDate='" + InfoTB_planEndDate + '\'' +
                ", InfoTB_planName='" + InfoTB_planName + '\'' +
                ", InfoTB_planTags='" + InfoTB_planTags + '\'' +
                ", InfoTB_titleImage='" + InfoTB_titleImage + '\'' +
                ", InfoTB_planCountry='" + InfoTB_planCountry + '\'' +
                '}';
    }

    public String getInfoTB_planDepartDate() {
        return InfoTB_planDepartDate;
    }

    public void setInfoTB_planDepartDate(String infoTB_planDepartDate) {
        InfoTB_planDepartDate = infoTB_planDepartDate;
    }

    public String getInfoTB_planEndDate() {
        return InfoTB_planEndDate;
    }

    public void setInfoTB_planEndDate(String infoTB_planEndDate) {
        InfoTB_planEndDate = infoTB_planEndDate;
    }

    public String getInfoTB_planName() {
        return InfoTB_planName;
    }

    public void setInfoTB_planName(String infoTB_planName) {
        InfoTB_planName = infoTB_planName;
    }

    public String getInfoTB_planTags() {
        return InfoTB_planTags;
    }

    public void setInfoTB_planTags(String infoTB_planTags) {
        InfoTB_planTags = infoTB_planTags;
    }

    public String getInfoTB_titleImage() {
        return InfoTB_titleImage;
    }

    public void setInfoTB_titleImage(String infoTB_titleImage) {
        InfoTB_titleImage = infoTB_titleImage;
    }

    public String getInfoTB_planCountry() {
        return InfoTB_planCountry;
    }

    public void setInfoTB_planCountry(String infoTB_planCountry) {
        InfoTB_planCountry = infoTB_planCountry;
    }
}
