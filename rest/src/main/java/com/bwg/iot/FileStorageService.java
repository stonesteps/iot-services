package com.bwg.iot;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Created by holow on 5/18/2016.
 */
@Service
public class FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String storeFile(final String key, final InputStream in, DBObject metaData) {
        LOGGER.debug("Storing file with key {}", key);

        final GridFSFile storedFile = gridFsTemplate.store(in, key, metaData);
        final String id;
        if (storedFile != null && storedFile.getId() != null) {
            id = storedFile.getId().toString();
            LOGGER.debug("File with key {} storred properly - file id is ", key, id);
        } else {
            id = null;
            LOGGER.error("Problems with saving file with key {} in mongo", key);
        }
        return id;
    }

    public InputStream getFileById(final String fileId) {
        LOGGER.debug("Storing file with id {}", fileId);

        final GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (gridFsdbFile != null) {
            return gridFsdbFile.getInputStream();
        }
        return null;
    }

    public InputStream getFileByKey(final String key) {
        LOGGER.debug("Storing file with key {}", key);

        final GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(key)));
        if (gridFsdbFile != null) {
            return gridFsdbFile.getInputStream();
        }
        return null;
    }

    public void delete(String fileId) {
        LOGGER.debug("Deleting file with id {}", fileId);
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
    }
}
