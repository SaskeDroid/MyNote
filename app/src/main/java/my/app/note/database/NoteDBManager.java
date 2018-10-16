package my.app.note.database;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCP on 2017.11.6.
 *
 */

public class NoteDBManager {

    private static volatile NoteDBManager noteDBManager;
    private Dao<NoteBean, ?> dao;

    private NoteDBManager(Context context) {
        try {
            dao = DataBaseHelper.getInstance(context).getDao(NoteBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static NoteDBManager getInstance(Context context) {
        if (noteDBManager == null) {
            synchronized (NoteDBManager.class) {
                if (noteDBManager == null) {
                    noteDBManager = new NoteDBManager(context.getApplicationContext());
                }
            }
        }
        return noteDBManager;
    }

    /* 查询所有笔记*/
    public List<NoteBean> selectNoteList() {
        if (dao != null) {
            try {
                return dao.queryBuilder().where().eq(NoteBean.NOTE_VISIBLE, 1).query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* 查询回收站笔记*/
    public List<NoteBean> selectRecycleList() {
        if (dao != null) {
            try {
                return dao.queryBuilder().where().eq(NoteBean.NOTE_VISIBLE, 0).query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* 查询带提醒时间的笔记*/
    public List<NoteBean> selectRemindList() {
        if (dao != null) {
            List<NoteBean> allList = selectNoteList();
            List<NoteBean> tempList = new ArrayList<>();
            for (NoteBean note : allList) {
                if (!TextUtils.isEmpty(note.getNoteRemindTime())) {
                    tempList.add(note);
                }
            }
            return tempList;
        }
        return null;
    }

    /* 根据标签查询笔记*/
    public List<NoteBean> selectLabelList(String label) {
        if (dao != null) {
            List<NoteBean> allList = selectNoteList();
            List<NoteBean> tempList = new ArrayList<>();
            for (NoteBean note : allList) {
                if (label.equals(note.getNoteTags())) {
                    tempList.add(note);
                }
            }
            return tempList;
        }
        return null;
    }

    /* 查询指定笔记*/
    public NoteBean select(int id) {
        if (id < 0) {
            return null;
        }
        if (dao != null) {
            try {
                return dao.queryBuilder().where().eq(NoteBean.NOTE_ID, id).queryForFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* 添加笔记*/
    public void insert(NoteBean note) {
        if (dao != null && note != null) {
            if (note.getNoteId() == 0) {
                NoteBean oldNote = select(note.getNoteId());
                note.setNoteVisible((byte) 1);
                if (oldNote != null) {
                    note.setNoteId(oldNote.getNoteId());
                }
            }
            try {
                dao.createOrUpdate(note);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* 修改笔记*/
    public void update(int id, NoteBean noteBean) {
        if (dao != null) {
            NoteBean note = select(id);
            if (note != null) {
                note.setNoteUpdateTime(noteBean.getNoteUpdateTime());
                note.setNoteContent(noteBean.getNoteContent());
                insert(note);
            }
        }
    }

    /* 插入提醒时间*/
    public void setRemind(int id, String timestamp) {
        if (dao != null) {
            NoteBean note = select(id);
            if (note != null) {
                note.setNoteRemindTime(timestamp);
                insert(note);
            }
        }
    }

    /* 插入标签*/
    public void setLabel(int id, String tags) {
        if (dao != null) {
            NoteBean note = select(id);
            if (note != null) {
                note.setNoteTags(tags);
                insert(note);
            }
        }
    }

    /* 删除到回收站*/
    public void recycle(int id) {
        if (dao != null) {
            NoteBean note = select(id);
            if (note != null) {
                note.setNoteVisible((byte) 0);
                insert(note);
            }
        }
    }

    /* 从回收站恢复*/
    public void revert(int id) {
        if (dao != null) {
            NoteBean note = select(id);
            if (note != null) {
                note.setNoteVisible((byte) 1);
                insert(note);
            }
        }
    }

    /* 彻底删除笔记*/
    public void delete(int id) {
        if (dao != null) {
            try {
                NoteBean note = select(id);
                if (note != null) {
                    dao.delete(note);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
