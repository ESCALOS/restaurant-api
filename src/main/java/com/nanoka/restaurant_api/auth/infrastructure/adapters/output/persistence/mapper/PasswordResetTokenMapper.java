    package com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.mapper;

    import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;
    import com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.entity.PasswordResetTokenEntity;
    import org.mapstruct.Mapper;

    @Mapper(componentModel = "spring")
    public interface PasswordResetTokenMapper {
        PasswordResetTokenEntity toPasswordResetTokenEntity(PasswordResetToken passwordResetToken);
        PasswordResetToken toPasswordResetToken(PasswordResetTokenEntity passwordResetTokenEntity);
    }
