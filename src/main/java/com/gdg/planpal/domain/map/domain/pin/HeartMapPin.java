package com.gdg.planpal.domain.map.domain.pin;

import com.gdg.planpal.domain.map.domain.IconType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("HEART")
public class HeartMapPin extends MapPin {
    @Override
    public IconType getIconType() {
        return IconType.HEART;
    }
}
