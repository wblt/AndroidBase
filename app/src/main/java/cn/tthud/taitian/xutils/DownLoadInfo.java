package cn.tthud.taitian.xutils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TianHongChun on 2016/5/8.
 * 文件下载实体类
 */
@Table(name = "download",onCreated = "CREATE UNIQUE INDEX index_name ON download(label,fileSavePath)")
public class DownLoadInfo {
    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "state")
    private  DownloadState state=DownloadState.STOPPED;//文件下载状态

    @Column(name = "url")
    private String url;

    @Column(name = "label")
    private String label;

    @Column(name = "fileSavePath")
    private String fileSavePath;//下载保存路径

    @Column(name = "progress")
    private int progress;//进度

    @Column(name = "fileLength")
    private long fileLength;//文件长度

    @Column(name = "autoResume")
    private boolean autoResume;//自动重新开始

    @Column(name = "autoRename")
    private boolean autoRename;//自动重命名


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    @Override
    public String toString() {
        return "DownLoadInfo{" +
                "id=" + id +
                ", state=" + state +
                ", url='" + url + '\'' +
                ", label='" + label + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", progress=" + progress +
                ", fileLength=" + fileLength +
                ", autoResume=" + autoResume +
                ", autoRename=" + autoRename +
                '}';
    }
}
