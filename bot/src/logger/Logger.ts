import fs from "fs";

export class Logger {
  private optionsData: Intl.DateTimeFormatOptions = {
    hour: "2-digit",
    minute: "2-digit",
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  };
  private logsPath: string;

  constructor(logsPath = "logs") {
    this.logsPath = logsPath;
  }

  private getDateFormatted() {
    const date = new Date();
    const formatter = new Intl.DateTimeFormat("pt-BR", this.optionsData);
    return formatter.format(date);
  }

  writeLog(message: string) {
    if (!fs.existsSync(this.logsPath)) fs.mkdirSync(this.logsPath, { recursive: true });
    const date = this.getDateFormatted();
    const textToWrite = `${date} - ${message}\n`;
    fs.writeFileSync(`${this.logsPath}/logs.log`, textToWrite, { flag: "a" });
  }
}
