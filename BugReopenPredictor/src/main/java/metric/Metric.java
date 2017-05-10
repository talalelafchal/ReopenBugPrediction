package metric;

/**
 * Created by Talal on 09.05.17.
 */
public class Metric {

    double stepToReproduce;
    double expectedBehaviour;
    double descriptionLength;
    double numberOfCommenter;
    double issueDeveloperCommitExperience;
    double issueDeveloperFixCommitExperience;
    double fixingDeveloperCommitExperience;
    double fixingDeveloperFixingExperience;
    double socialStrength;
    double locFixMean;
    double locFixMedian;
    double deltaWMCmean;
    double delaWMCmedian;
    double readabilityMean;
    double readabilityMedian;
    double cdMean;
    double cdMedian;
    double CBOMean;
    double CBOMedian;
    double DITMean;
    double DITMedian;
    double locPreFixMean;
    double locPreFixMedian;
    double LCOMMean;
    double LCOMMedian;
    double NOCMean;
    double NOCMedian;
    double NOFMean;
    double NOFMedian;
    double NOMMean;
    double NOMMedian;
    double NOPMMean;
    double NOPMMedian;
    double NOPFMean;
    double NOPFMedian;
    double NOSMMean;
    double NOSMMedian;
    double NOSFMean;
    double NOSFMedian;
    double NOSIMean;
    double NOSIMedian;
    double RFCMean;
    double RFCMedian;
    double wmcPreFixMean;
    double wmcPreFixMedian;
    boolean reopen;

    public Metric(double stepToReproduce, double expectedBehaviour, double descriptionLength, double numberOfCommenter, double issueDeveloperCommitExperience, double issueDeveloperFixCommitExperience, double fixingDeveloperCommitExperience, double fixingDeveloperFixingExperience, double socialStrength, double locFixMean, double locFixMedian, double deltaWMCmean, double delaWMCmedian, double readabilityMean, double readabilityMedian, double cdMean, double cdMedian, double CBOMean, double CBOMedian, double DITMean, double DITMedian, double locPreFixMean, double locPreFixMedian, double LCOMMean, double LCOMMedian, double NOCMean, double NOCMedian, double NOFMean, double NOFMedian, double NOMMean, double NOMMedian, double NOPMMean, double NOPMMedian, double NOPFMean, double NOPFMedian, double NOSMMean, double NOSMMedian, double NOSFMean, double NOSFMedian, double NOSIMean, double NOSIMedian, double RFCMean, double RFCMedian, double wmcPreFixMean, double wmcPreFixMedian, boolean reopen) {
        this.stepToReproduce = stepToReproduce;
        this.expectedBehaviour = expectedBehaviour;
        this.descriptionLength = descriptionLength;
        this.numberOfCommenter = numberOfCommenter;
        this.issueDeveloperCommitExperience = issueDeveloperCommitExperience;
        this.issueDeveloperFixCommitExperience = issueDeveloperFixCommitExperience;
        this.fixingDeveloperCommitExperience = fixingDeveloperCommitExperience;
        this.fixingDeveloperFixingExperience = fixingDeveloperFixingExperience;
        this.socialStrength = socialStrength;
        this.locFixMean = locFixMean;
        this.locFixMedian = locFixMedian;
        this.deltaWMCmean = deltaWMCmean;
        this.delaWMCmedian = delaWMCmedian;
        this.readabilityMean = readabilityMean;
        this.readabilityMedian = readabilityMedian;
        this.cdMean = cdMean;
        this.cdMedian = cdMedian;
        this.CBOMean = CBOMean;
        this.CBOMedian = CBOMedian;
        this.DITMean = DITMean;
        this.DITMedian = DITMedian;
        this.locPreFixMean = locPreFixMean;
        this.locPreFixMedian = locPreFixMedian;
        this.LCOMMean = LCOMMean;
        this.LCOMMedian = LCOMMedian;
        this.NOCMean = NOCMean;
        this.NOCMedian = NOCMedian;
        this.NOFMean = NOFMean;
        this.NOFMedian = NOFMedian;
        this.NOMMean = NOMMean;
        this.NOMMedian = NOMMedian;
        this.NOPMMean = NOPMMean;
        this.NOPMMedian = NOPMMedian;
        this.NOPFMean = NOPFMean;
        this.NOPFMedian = NOPFMedian;
        this.NOSMMean = NOSMMean;
        this.NOSMMedian = NOSMMedian;
        this.NOSFMean = NOSFMean;
        this.NOSFMedian = NOSFMedian;
        this.NOSIMean = NOSIMean;
        this.NOSIMedian = NOSIMedian;
        this.RFCMean = RFCMean;
        this.RFCMedian = RFCMedian;
        this.wmcPreFixMean = wmcPreFixMean;
        this.wmcPreFixMedian = wmcPreFixMedian;
        this.reopen = reopen;
    }

    public double getStepToReproduce() {
        return stepToReproduce;
    }

    public double getExpectedBehaviour() {
        return expectedBehaviour;
    }

    public double getDescriptionLength() {
        return descriptionLength;
    }

    public double getNumberOfCommenter() {
        return numberOfCommenter;
    }

    public double getIssueDeveloperCommitExperience() {
        return issueDeveloperCommitExperience;
    }

    public double getIssueDeveloperFixCommitExperience() {
        return issueDeveloperFixCommitExperience;
    }

    public double getFixingDeveloperCommitExperience() {
        return fixingDeveloperCommitExperience;
    }

    public double getFixingDeveloperFixingExperience() {
        return fixingDeveloperFixingExperience;
    }

    public double getSocialStrength() {
        return socialStrength;
    }

    public double getLocFixMean() {
        return locFixMean;
    }

    public double getLocFixMedian() {
        return locFixMedian;
    }

    public double getDeltaWMCmean() {
        return deltaWMCmean;
    }

    public double getDelaWMCmedian() {
        return delaWMCmedian;
    }

    public double getReadabilityMean() {
        return readabilityMean;
    }

    public double getReadabilityMedian() {
        return readabilityMedian;
    }

    public double getCdMean() {
        return cdMean;
    }

    public double getCdMedian() {
        return cdMedian;
    }

    public double getCBOMean() {
        return CBOMean;
    }

    public double getCBOMedian() {
        return CBOMedian;
    }

    public double getDITMean() {
        return DITMean;
    }

    public double getDITMedian() {
        return DITMedian;
    }

    public double getLocPreFixMean() {
        return locPreFixMean;
    }

    public double getLocPreFixMedian() {
        return locPreFixMedian;
    }

    public double getLCOMMean() {
        return LCOMMean;
    }

    public double getLCOMMedian() {
        return LCOMMedian;
    }

    public double getNOCMean() {
        return NOCMean;
    }

    public double getNOCMedian() {
        return NOCMedian;
    }

    public double getNOFMean() {
        return NOFMean;
    }

    public double getNOFMedian() {
        return NOFMedian;
    }

    public double getNOMMean() {
        return NOMMean;
    }

    public double getNOMMedian() {
        return NOMMedian;
    }

    public double getNOPMMean() {
        return NOPMMean;
    }

    public double getNOPMMedian() {
        return NOPMMedian;
    }

    public double getNOPFMean() {
        return NOPFMean;
    }

    public double getNOPFMedian() {
        return NOPFMedian;
    }

    public double getNOSMMean() {
        return NOSMMean;
    }

    public double getNOSMMedian() {
        return NOSMMedian;
    }

    public double getNOSFMean() {
        return NOSFMean;
    }

    public double getNOSFMedian() {
        return NOSFMedian;
    }

    public double getNOSIMean() {
        return NOSIMean;
    }

    public double getNOSIMedian() {
        return NOSIMedian;
    }

    public double getRFCMean() {
        return RFCMean;
    }

    public double getRFCMedian() {
        return RFCMedian;
    }

    public double getWmcPreFixMean() {
        return wmcPreFixMean;
    }

    public double getWmcPreFixMedian() {
        return wmcPreFixMedian;
    }

    public boolean isReopen() {
        return reopen;
    }
}
