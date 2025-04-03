package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationDocumentEntity;
import com.backend.legisloop.entities.LegislationEntity;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@AllArgsConstructor
@Builder
@Data
@Slf4j
public class LegislationDocument {

    private int docId;
    private int billId;
    private String textHash;
    private URI legiscanLink;
    private URI externalLink;
    private String mime;
    private int mimeId;
    private String docContent;
    private String type;
    private int typeId;

    public LegislationDocumentEntity toEntity() {
        return LegislationDocumentEntity.builder()
                .doc_id(this.docId)
                .bill(LegislationEntity.builder().bill_id(this.billId).build())
                .text_hash(this.textHash)
                .url(this.legiscanLink)
                .state_link(this.externalLink)
                .mime(this.mime)
                .mimeId(this.mimeId)
                .docContent(this.docContent)
                .type(this.type)
                .type_id(this.typeId)
                .build();
    }
    
    public static LegislationDocument fillDocument(JsonObject textObject) {
    	LegislationDocumentBuilder documentBuilder = LegislationDocument.builder()
	        .textHash(textObject.get("text_hash").getAsString())
	        .legiscanLink(URI.create(textObject.get("url").getAsString()))
	        .externalLink(URI.create(textObject.get("state_link").getAsString()))
	        .docId(textObject.get("doc_id").getAsInt())
	        .docContent(textObject.get("doc") == null ? null : textObject.get("doc").getAsString())
	        .mime(textObject.get("mime").getAsString())
	        .mimeId(textObject.get("mime_id").getAsInt())
	        .type(textObject.get("type").getAsString())
	        .typeId(textObject.get("type_id").getAsInt());
    	
    	if (textObject.get("bill_id") != null) {
    		documentBuilder.billId(textObject.get("bill_id").getAsInt());
        }
        return documentBuilder.build();
    }

}
