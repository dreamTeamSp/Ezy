package com.example.ezymobile;

public class cursos {
    //Atributos da classe
    private String titulo;
    private String desc;
    private String img;
    private String codigo;

    //Construtores da classe Produto
    public cursos( String titulo, String desc, String img) {
        this.titulo = titulo;
        this.desc = desc;
        this.img = img;
    }

    public cursos(String codigo, String titulo, String desc, String img) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.desc = desc;
        this.img = img;
    }
    //Construtor vazio
    public cursos() {  }

    //Getters e Setters
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg() {
        return img;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
