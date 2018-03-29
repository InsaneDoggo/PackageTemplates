package base;

import global.wrappers.BinaryFileWrapper;
import global.wrappers.DirectoryWrapper;
import global.wrappers.FileWrapper;

/**
 * Created by Arsen on 11.09.2016.
 */
public interface ElementVisitor {

    void visit(DirectoryWrapper wrapper);

    void visit(FileWrapper wrapper);

    void visit(BinaryFileWrapper wrapper);

}
