namespace java org.sangraama.thrift.transmissionservice

include "player.thrift"

service PlayerTransmissionService{
	
	oneway void passPlayer(1: player.Player player)
	
}