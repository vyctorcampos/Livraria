package com.desafio.livraria.util;

public class Utilitarios {

    public static boolean isNomeNuloOuVazio(String nome) {
        return nome == null || nome.trim().isEmpty();
    }

    public static String formatarDataHora(java.time.LocalDateTime dataHora) {
        if (dataHora == null) {
            return "";
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHora.format(formatter);
    }

    public static String formatarData(java.time.LocalDate data) {
        if (data == null) {
            return "";
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    public static java.time.LocalDateTime parseDataHora(String dataHoraStr) {
        if (dataHoraStr == null || dataHoraStr.trim().isEmpty()) {
            return null;
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            return java.time.LocalDateTime.parse(dataHoraStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static java.time.LocalDate parseData(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            return null;
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return java.time.LocalDate.parse(dataStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

}
