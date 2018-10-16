package my.app.note.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by CCP on 2017.11.6.
 *
 */

@DatabaseTable(tableName = "table_note")
public class NoteBean implements BaseColumns, Parcelable {

    static final String NOTE_ID = _ID;
    static final String NOTE_CREATE_TIME = "NOTE_CREATE_TIME";
    static final String NOTE_UPDATE_TIME = "NOTE_UPDATE_TIME";
    static final String NOTE_CONTENT = "NOTE_CONTENT";
    static final String NOTE_VISIBLE = "NOTE_VISIBLE";
    static final String NOTE_REMIND_TIME = "NOTE_REMIND_TIME";
    static final String NOTE_TAGS = "NOTE_TAGS";

    @DatabaseField(columnName = NOTE_ID, generatedId = true)
    private int noteId;

    @DatabaseField(columnName = NOTE_CREATE_TIME, canBeNull = false, unique = true)
    private String noteCreateTime;

    @DatabaseField(columnName = NOTE_UPDATE_TIME)
    private String noteUpdateTime;

    @DatabaseField(columnName = NOTE_CONTENT, canBeNull = false)
    private String noteContent;

    @DatabaseField(columnName = NOTE_VISIBLE)
    private byte noteVisible;

    @DatabaseField(columnName = NOTE_REMIND_TIME)
    private String noteRemindTime;

    @DatabaseField(columnName = NOTE_TAGS)
    private String noteTags;

    // 注：ORMLite建表的类需要一个无参构造方法
    public NoteBean() {}

    private NoteBean(Parcel in) {
        noteId = in.readInt();
        noteCreateTime = in.readString();
        noteUpdateTime = in.readString();
        noteContent = in.readString();
        noteVisible = in.readByte();
        noteRemindTime = in.readString();
        noteTags = in.readString();
    }

    public static final Creator<NoteBean> CREATOR = new Creator<NoteBean>() {
        @Override
        public NoteBean createFromParcel(Parcel in) {
            return new NoteBean(in);
        }

        @Override
        public NoteBean[] newArray(int size) {
            return new NoteBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(noteId);
        dest.writeString(noteCreateTime);
        dest.writeString(noteUpdateTime);
        dest.writeString(noteContent);
        dest.writeByte(noteVisible);
        dest.writeString(noteRemindTime);
        dest.writeString(noteTags);
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteCreateTime() {
        return noteCreateTime;
    }

    public void setNoteCreateTime(String noteCreateTime) {
        this.noteCreateTime = noteCreateTime;
    }

    public String getNoteUpdateTime() {
        return noteUpdateTime;
    }

    public void setNoteUpdateTime(String noteUpdateTime) {
        this.noteUpdateTime = noteUpdateTime;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public byte getNoteVisible() {
        return noteVisible;
    }

    public void setNoteVisible(byte noteVisible) {
        this.noteVisible = noteVisible;
    }

    public String getNoteRemindTime() {
        return noteRemindTime;
    }

    public void setNoteRemindTime(String noteRemindTime) {
        this.noteRemindTime = noteRemindTime;
    }

    public String getNoteTags() {
        return noteTags;
    }

    public void setNoteTags(String noteTags) {
        this.noteTags = noteTags;
    }
}
