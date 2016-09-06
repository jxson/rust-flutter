PHONY: all
all:
	cd android
	gradle wrapper
	./gradlew build
