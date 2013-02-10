package logisticspipes.logisticspipes;

import logisticspipes.interfaces.ILogisticsModule;
import logisticspipes.pipes.PipeLogisticsChassi;
import logisticspipes.utils.SinkReply;
import net.minecraftforge.common.ForgeDirection;

public class ChassiTransportLayer extends TransportLayer{

	private final PipeLogisticsChassi _chassiPipe;
	
	public ChassiTransportLayer(PipeLogisticsChassi chassiPipe) {
		_chassiPipe = chassiPipe;
	}

	@Override
	public ForgeDirection itemArrived(IRoutedItem item) {
		item.setArrived(true);
		return _chassiPipe.getPointedOrientation();
	}

	@Override
	public boolean stillWantItem(IRoutedItem item) {
		ILogisticsModule module = _chassiPipe.getLogisticsModule();
		if (module == null) return false;
		if (!_chassiPipe.isEnabled()) return false;
		SinkReply reply = module.sinksItem(item.getIDStack().getItem(), -1, 0);
		if (reply == null) return false;
		
		if (reply.maxNumberOfItems != 0 && item.getItemStack().stackSize > reply.maxNumberOfItems){
			ForgeDirection o = _chassiPipe.getPointedOrientation();
			if (o==null || o == ForgeDirection.UNKNOWN) o = ForgeDirection.UP;
			
			item.split(_chassiPipe.worldObj, reply.maxNumberOfItems, o);
		}
		return true;
	}

}
