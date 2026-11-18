package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.model.Video;

public interface VideoDao {
    void insert(Video video, int userId);
    void edit(Video video);
    boolean deleteOwned(int videoId, int userId);
    Video get(int videoId);
    Video getOwned(int videoId, int userId);
    List<Video> getByUser(int userId, int offset, int limit);
    long countByUser(int userId);
    List<Video> getLatest(int limit);
}
