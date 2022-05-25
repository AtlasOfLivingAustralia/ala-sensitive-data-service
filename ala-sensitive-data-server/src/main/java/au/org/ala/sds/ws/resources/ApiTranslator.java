package au.org.ala.sds.ws.resources;

import au.org.ala.sds.api.*;
import au.org.ala.sds.model.ConservationInstance;
import au.org.ala.sds.model.Message;
import au.org.ala.sds.model.PlantPestInstance;
import au.org.ala.sds.validation.SdsValidationReport;
import au.org.ala.sds.validation.ValidationOutcome;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts SDS model objects into sensitive data service objects suitable for
 * transmission.
 */
public class ApiTranslator {
    /** The key to the original values in the validation results */
    public static String ORIGINAL_VALUES = "originalSensitiveValues";

    /** Keep the SDS library original/translated values (if false, the generalisation rules are used instead) */
    private boolean useLibrary;

    /**
     * Construct a translator.
     *
     * @param useLibrary Use the SDS libraries values in preference to the generalisations.
     */
    public ApiTranslator(boolean useLibrary) {
        this.useLibrary = useLibrary;
    }

    /**
     * Generate a sensitivity report from the SDS outcome.
     */
    public SensitivityReport buildSensitivityReport(ValidationOutcome outcome) {
        if (outcome == null)
            return null;
        Map<String, Object> original = this.useLibrary && outcome.getResult() != null ? (Map<String, Object>) outcome.getResult().remove(ORIGINAL_VALUES) : new HashMap<>();
        Map<String, Object> result = this.useLibrary ? outcome.getResult() : new HashMap<>();
        return SensitivityReport.builder()
            .valid(outcome.isValid())
            .sensitive(outcome.isSensitive())
            .loadable(outcome.isLoadable())
            .accessControl(outcome.isControlledAccess())
            .instances(outcome.getInstances().stream().map(i -> this.buildSensitivityInstance(i)).collect(Collectors.toList()))
            .report(this.buildValidationReport(outcome.getReport()))
            .original(original)
            .updated(result)
            .build();
    }

    /**
     * Generate a validation report from an SDS validation report.
     */
    public ValidationReport buildValidationReport(au.org.ala.sds.validation.ValidationReport report) {
        if (report == null)
            return null;
        ValidationReport.ValidationReportBuilder builder = ValidationReport.builder()
            .messages(report.getMessages().stream().map(m -> this.buildMessage(m)).collect(Collectors.toList()))
            .category(report.getCategory())
            .assertion(report.getAssertion());
        if (report instanceof SdsValidationReport)
            builder.taxon(this.buildSensitiveTaxon(((SdsValidationReport) report).getSpecies()));
        return builder.build();
    }

    /**
     * Generate a new message from an SDS message.
     */
    public ValidationReport.Message buildMessage(Message message) {
        if (message == null)
            return null;
        return new ValidationReport.Message(message.getType().name(), message.getCategory(), message.getMessageText());
    }

    /**
     * Generate a sensitive taxon description from the SDS
     */
    public SensitiveTaxon buildSensitiveTaxon(au.org.ala.sds.model.SensitiveTaxon taxon) {
        if (taxon == null)
            return null;
        return SensitiveTaxon.builder()
            .scientificName(taxon.getName())
            .taxonId(taxon.getLsid())
            .family(taxon.getFamily())
            .acceptedName(taxon.getAcceptedName())
            .commonName(taxon.getCommonName())
            .taxonRank(taxon.getRank() == null ? null : taxon.getRank().getRank())
            .instances(taxon.getInstances().stream().map(i -> this.buildSensitivityInstance(i)).collect(Collectors.toList()))
            .build();
    }

    /**
     * Generate a sensitivity instance from the SDS
     */
    public SensitivityInstance buildSensitivityInstance(au.org.ala.sds.model.SensitivityInstance instance) {
        if (instance == null)
            return null;
        SensitivityInstance.SensitivityInstanceBuilder builder = SensitivityInstance.builder()
            .category(this.buildSensitivityCategory(instance.getCategory()))
            .zone(this.buildSensitivityZone(instance.getZone()))
            .authority(instance.getAuthority())
            .dataResourceId(instance.getDataResourceId())
            .reason(instance.getReason())
            .remarks(instance.getRemarks());
        if (instance instanceof ConservationInstance) {
            ConservationInstance ci = (ConservationInstance) instance;
            builder.type(SensitivityInstance.SensitivityType.CONSERVATION);
            builder.generalisation(new GeneralisationRule(ci.getLocationGeneralisation()));
        } else if (instance instanceof PlantPestInstance) {
            PlantPestInstance pi = (PlantPestInstance) instance;
            builder.type(SensitivityInstance.SensitivityType.PEST);
            builder.fromDate(pi.getFromDate());
            builder.toDate(pi.getToDate());
            builder.transientEvents(pi.getTransientEventList() == null ? null : pi.getTransientEventList().stream().map(te -> this.buildTransientEvent(te)).collect(Collectors.toList()));
        }
        return builder.build();
    }

    /**
     * Generate a sensitivity category from the SDS
     */
    public SensitivityCategory buildSensitivityCategory(au.org.ala.sds.model.SensitivityCategory category) {
        if (category == null)
            return null;
        return new SensitivityCategory(category.getId(), category.getValue(), category.getType() == null ? null : category.getType().name());
    }

    /**
     * Generate a sensitivity zone from the SDS
     */
    public SensitivityZone buildSensitivityZone(au.org.ala.sds.model.SensitivityZone zone) {
        if (zone == null)
            return null;
        return new SensitivityZone(zone.getId(), zone.getName(), zone.getLayerId(), zone.getType() == null ? null : zone.getType().name());
    }

    public TransientEvent buildTransientEvent(au.org.ala.sds.model.PlantPestInstance.TransientEvent event) {
        if (event == null)
            return null;
        SensitivityZone zone = this.buildSensitivityZone(event.getZone());
        return TransientEvent.builder().eventDate(event.getEventDate()).zone(zone).build();

    }
}
