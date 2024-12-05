import Session from "../models/Session";

class SessionRepository {
  static createSession(sessionId: string): Session {
    return new Session(sessionId);
  }
}

export default SessionRepository;
