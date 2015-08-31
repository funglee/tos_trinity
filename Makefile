COMPONENT=TosTrinityDemoAppC
BUILD_EXTRA_DEPS+=TosTrinityDemoC.class
CLEAN_EXTRA=*.class *.java


TosTrinityDemoC.class: $(wildcard *.java) VolReadMsg.java
	javac -target 1.4 -source 1.4 *.java

VolReadMsg.java:
	mig java -target=null $(CFLAGS) -java-classname=VolReadMsg VolReadMsg.h VolReadMsg -o $@

	

#PFLAGS += -DTIMESYNC_RATE=3

#PFLAGS += -I$(TOSDIR)/lib/ftsp

#CFLAGS += -DPACKET_LINK

#PFLAGS += -I$(TOSDIR)/sensorboards/mts300

include $(MAKERULES)

