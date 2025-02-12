interface GlobalExceptionProps {
  message: string;
  statusCode: number;
  details: string;
}

export class GlobalException extends Error {
  public statusCode: number;
  public details: string;

  constructor({ message, statusCode, details }: GlobalExceptionProps) {
    super(message);
    this.statusCode = statusCode;
    this.details = details;
  }
}
