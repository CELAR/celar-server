package gr.ntua.cslab.celar.server.beans.structured;
/**
 *
 * @author cmantas
 */
 interface Structured {
    public String toStucturedString();
    
    public String toStructuredString(int indent);
}
