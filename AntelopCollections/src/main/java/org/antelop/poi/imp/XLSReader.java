package org.antelop.poi.imp;

import java.io.File;
import java.io.InputStream;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

public class XLSReader
{
    private IReader mRowReader;
    
    public IReader getmRowReader()
    {
        return mRowReader;
    }

    public void setmRowReader(IReader mRowReader)
    {
        this.mRowReader = mRowReader;
    }
    private int tempRowId;
    
    private int mLastCol;
    
    private int mSheetIndex;
    
//    private List<String> mRowList = new ArrayList<String>();
    private Boolean isExistTitle;

    public void Process(String fileName, int sheetId) throws Exception
    {
        NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(fileName));
        InputStream input = fs.createDocumentInputStream("Workbook");
        HSSFRequest request = new HSSFRequest();   //这儿为所有类型的Record都注册了监听器，如果需求明确的话，可以用addListener方法，并指定所需的Record类型
        request.addListenerForAllRecords(new HSSFListener() {
            
            private SSTRecord sstrec;
            
            @SuppressWarnings("static-access")
            @Override
            public void processRecord(Record record)
            {
                switch (record.getSid())
                {
                    // the BOFRecord can represent either the beginning of a sheet or the workbook
                    case BOFRecord.sid:
                        BOFRecord bof = (BOFRecord) record;
                        if (bof.getType() == bof.TYPE_WORKBOOK)
                        {
//                            System.out.println("Encountered workbook");
                            // assigned to the class level member
                        } else if (bof.getType() == bof.TYPE_WORKSHEET)
                        {
//                            System.out.println("Encountered sheet reference");
                        }
                        break;
                    case BoundSheetRecord.sid:
                        BoundSheetRecord bsr = (BoundSheetRecord) record;
//                        System.out.println("New sheet named: " + bsr.getSheetname());
                        mSheetIndex++;
                        System.out.println(mSheetIndex);
                        break;
                    case RowRecord.sid:
                        RowRecord rowrec = (RowRecord) record;
//                        System.err.println(rowrec.getRecordSize());
                       /* System.out.println("RowNumber found, column at " + rowrec.getRowNumber() +
                            " Row found, first column at "
                                + rowrec.getFirstCol() + " last column at " + rowrec.getLastCol());*/
                        break;
                    case NumberRecord.sid: //单元格为数字类型  
                        NumberRecord numrec = (NumberRecord) record;
                        /*System.out.println("Cell found with value " + numrec.getValue()
                                + " at row " + numrec.getRow() + " and column " + numrec.getColumn());*/
                        break;
                        // SSTRecords store a array of unique strings used in Excel.
                    case SSTRecord.sid:
                        sstrec = (SSTRecord) record;
                        for (int k = 0; k < sstrec.getNumUniqueStrings(); k++)
                        {
//                            System.out.println("String table value " + k + " = " + sstrec.getString(k));
                        }
                        break;
                    case LabelSSTRecord.sid: //单元格为字符串类型
                        
                        LabelSSTRecord lrec = (LabelSSTRecord) record;
                        
                        /*if(tempRowId != lrec.getRow()){
                            try {
                                mRowReader.RowsHandler(mSheetIndex, lrec.getRow(), mRowList, isExistTitle);
                                mRowList.clear();
                            }
                            catch (SAXException e) {
                                e.printStackTrace();
                            }
                        }
                        mRowList.add(sstrec.getString(lrec.getSSTIndex())+"");*/
//                        System.out.println(lrec.getSSTIndex() + "-" + "String cell found with value "
//                                + sstrec.getString(lrec.getSSTIndex()));
//                        tempRowId = lrec.getRow();
                        break;
                }
            }
        });
        
        HSSFEventFactory factory = new HSSFEventFactory(); 
        factory.processEvents(request, input);
        fs.close();
    }
}
