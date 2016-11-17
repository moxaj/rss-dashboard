package rss_dashboard.server.network.misc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.ILoginRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class LoginRequest implements ILoginRequest {
	private final String username;
	private final String password;
}
