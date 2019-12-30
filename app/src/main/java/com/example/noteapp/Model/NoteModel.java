package com.example.noteapp.Model;

public class NoteModel {
    String categoryId,title,note,date,noteId;

    public NoteModel() {
    }

    public NoteModel(String categoryId, String title, String note, String date) {
        this.categoryId = categoryId;
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
