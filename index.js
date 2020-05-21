import { NativeModules } from "react-native";

const { AppodealModule } = NativeModules;

const eventEmitter = new NativeEventEmitter(ChartboostModule);

const _subscriptions = new Map();

const addEventListener = (event, handler) => {
  const mappedEvent = event;

  let listener;

  listener = eventEmitter.addListener(mappedEvent, handler);

  _subscriptions.set(handler, listener);

  return {
    remove: () => {
      try {
        const listener = _subscriptions.get(handler);

        if (!listener) {
          return;
        }

        listener.remove();

        _subscriptions.delete(handler);

        return;
      } catch (error) {
        console.log("Error in removeEventListener: " + event, error);
        return;
      }
    },
  };
};

export default {
  ...AppodealModule,
  addEventListener,
};
