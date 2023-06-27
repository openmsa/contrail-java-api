package net.juniper.contrail.api;

public abstract class Status {
	private Status() {
	}

	public abstract boolean isSuccess();

	public abstract void ifFailure(ErrorHandler handler);

	private static final Status success = new Success();

	public static Status success() {
		return success;
	}

	public static Status failure(final String message) {
		return new Failure(message);
	}

	private static class Success extends Status {
		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public void ifFailure(final ErrorHandler handler) {
			// do nothing
		}
	}

	private static class Failure extends Status {
		private final String message;

		private Failure(final String message) {
			this.message = message;
		}

		@Override
		public boolean isSuccess() {
			return false;
		}

		@Override
		public void ifFailure(final ErrorHandler handler) {
			handler.handle(message);
		}
	}

	public interface ErrorHandler {
		void handle(String errorMessage);
	}
}
