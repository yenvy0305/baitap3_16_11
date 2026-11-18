package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.VideoDao;
import vn.iotstar.dao.impl.VideoDaoImpl;
import vn.iotstar.model.Video;
import vn.iotstar.service.VideoService;

public class VideoServiceImpl implements VideoService {
    private final VideoDao dao = new VideoDaoImpl();

    @Override
    public void insert(Video video, int userId) {
        dao.insert(video, userId);
    }

    @Override
    public void edit(Video video) {
        dao.edit(video);
    }

    @Override
    public boolean deleteOwned(int videoId, int userId) {
        return dao.deleteOwned(videoId, userId);
    }

    @Override
    public Video get(int videoId) {
        return dao.get(videoId);
    }

    @Override
    public Video getOwned(int videoId, int userId) {
        return dao.getOwned(videoId, userId);
    }

    @Override
    public List<Video> getByUser(int userId, int offset, int limit) {
        return dao.getByUser(userId, offset, limit);
    }

    @Override
    public long countByUser(int userId) {
        return dao.countByUser(userId);
    }

    @Override
    public List<Video> getLatest(int limit) {
        return dao.getLatest(limit);
    }
}
