package Service;

import java.util.List;
import Model.ModelPenjualan;

public interface ServicePenjualan {
    void tambahData     (ModelPenjualan model);
    void perbaruiData   (ModelPenjualan model);
    void hapusData      (ModelPenjualan model);
    
    List<ModelPenjualan>    tampilData();
    List<ModelPenjualan>    pencarianData(String id);
    List<ModelPenjualan>    pencarianDataByBarcode(String id5);
}
