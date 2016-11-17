package rss_dashboard.server.network;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rss_dashboard.common.network.IAuthenticatedRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@AllArgsConstructor
@Getter
public class AuthenticatedRequest implements IAuthenticatedRequest {
	private final String token;
}
