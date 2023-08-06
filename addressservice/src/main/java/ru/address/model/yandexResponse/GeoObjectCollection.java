package ru.address.model.yandexResponse;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GeoObjectCollection {
    private MetaDataProperty metaDataProperty;

    private List<FeatureMember> featureMember;
}
