package de.yaacc.upnp;

import java.util.List;

import org.teleal.cling.model.XMLUtil;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.support.contentdirectory.callback.Browse;
import org.teleal.cling.support.contentdirectory.callback.Browse.Status;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.BrowseResult;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;
/**
 * 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 3
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
/**
 * Browser for ContentDirectories. 
 * Connect an instance of this class to a MediaServer-Service.
 * After calling run you will browse the MediaServer-Directory asynchronously 
 * @author Tobias Schöne (openbit)  
 *
 */
public class ContentDirectoryBrowseResult extends Browse {
	private Status status = Status.NO_CONTENT;
	private DIDLContent result  = null;
	private UpnpFailure upnpFailure;
	

	
	public ContentDirectoryBrowseResult(Service<?, ?> service, String objectID,
			BrowseFlag flag, String filter, long firstResult, Long maxResults,
			SortCriterion... orderBy) {
		super(service, objectID, flag, filter, firstResult, maxResults, orderBy);

	}

	
	public ContentDirectoryBrowseResult(Service<?, ?> service, String containerId,
			BrowseFlag flag) {
		super(service, containerId, flag);

	}

	@Override
	public void received(ActionInvocation actionInvocation, DIDLContent didl) {
		result = didl;
	}
	

	@Override
	public void updateStatus(Status status) {
		this.status = status;
	}

	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		this.upnpFailure= new UpnpFailure(invocation, operation, defaultMsg);

	}

	public Status getStatus() {
		return status;
	}


	/**
	 * @return the result
	 */
	public DIDLContent getResult() {
		return result;
	}


	/**
	 * @return the upnpFailure
	 */
	public UpnpFailure getUpnpFailure() {
		return upnpFailure;
	}

}