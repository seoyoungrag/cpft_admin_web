import React from "react";
import "./NoteList.css";
import NoteItem from "../NoteItem/NoteItem";

const cx = (txt) => {
  return txt;
};

const NoteList = ({
  notes,
  editing,
  onToggle,
  onChange,
  onUpdate,
  onDelete,
}) => {
  const noteList = notes.map((note, i) => {
    return (
      <NoteItem
        note={note}
        key={note.id}
        editing={editing}
        onToggle={onToggle}
        onChange={onChange}
        onUpdate={onUpdate}
        onDelete={onDelete}
      />
    );
  });
  return (
    <div className={cx("note-list")}>
      <div className={cx("title")}>Your Notes...</div>
      {noteList}
    </div>
  );
};

export default NoteList;
