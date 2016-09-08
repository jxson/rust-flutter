
.PHONY: all
all: build-android build-rust

.PHONY: build-android
build-android: build-rust
	cd android && ./gradlew assembleRelease

.PHONY: build-rust
build-rust:
	cd libsample && cargo build --target armv7-linux-androideabi --release
