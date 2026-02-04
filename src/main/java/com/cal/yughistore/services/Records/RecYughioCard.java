package com.cal.yughistore.services.Records;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RecYughioCard(int apiId, String name, String type, String frameType, String race, String desc, String ygoprodeck_url) {
}
