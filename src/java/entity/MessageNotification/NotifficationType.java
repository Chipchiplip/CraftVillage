/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.MessageNotification;

/**
 *
 * @author ACER
 */
public class NotifficationType {

    private int typeID;
    private String typeName;

    // Constructors-------------------------------------------------------------
    public NotifficationType() {
    }

    //--------------------------------------------------------------------------
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "NotifficationType{" + "typeID=" + typeID + ", typeName=" + typeName + '}';
    }

}
