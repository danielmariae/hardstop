package br.unitins.topicos1.model.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Perfil {
    USER(0, "User"),
    FUNC(1, "Func"),
    ADMIN(2, "Admin");

    private final Integer id;
    private final String label;

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    Perfil(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public static Perfil valueOf(Integer id) {
        if(id == null) return null;
        for(Perfil perfil: Perfil.values()) {
            if(perfil.getId().equals(id)) return perfil;
        }
        throw new IllegalArgumentException("id inválido" + id);
    }
}

