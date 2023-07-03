package net.sharecs.city.sharecsservice.business.z_common.model.embed_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentInfo implements Serializable {
    
    private String fileId;

    private String fileName;

}
