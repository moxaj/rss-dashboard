package rss_dashboard.server.network.misc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.ILoginResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class LoginResponse implements ILoginResponse {
	private final String token;
}
