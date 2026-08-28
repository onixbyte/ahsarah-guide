package com.onixbyte.ahsarahguide.specification;

import com.onixbyte.ahsarahguide.domain.entity.Modification;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ModificationSpecification {

    /**
     * Filters by firearm ID if specified.
     */
    public static Specification<Modification> hasFirearmId(Long firearmId) {
        return (root, query, cb) -> {
            if (Objects.isNull(firearmId)) {
                return null;
            }
            return cb.equal(root.get("firearm").get("id"), firearmId);
        };
    }

    /**
     * Checks if the jsonb tags contain the given JSON string.
     */
    public static Specification<Modification> containsTags(String tagsJson) {
        return (root, query, cb) -> {
            if (Objects.isNull(tagsJson)) {
                return null;
            }
            // Invoke the PostgreSQL native function 'jsonb_contains' (backing the
            // '@>' operator). The argument is cast via the 'jsonb' function-style
            // cast so PostgreSQL resolves the correct overload instead of treating
            // the bound String as varchar.
            return cb.isTrue(
                    cb.function("jsonb_contains", Boolean.class,
                            root.get("tags"),
                            cb.function("jsonb", String.class, cb.literal(tagsJson)))
            );
        };
    }
}
