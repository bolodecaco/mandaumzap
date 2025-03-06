import { format, parseISO, isToday, isYesterday } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export const formatLastSentAt = (dateString: string) => {
  const date = parseISO(dateString)
  if (isToday(date)) {
    return `hoje, ${format(date, 'HH:mm', { locale: ptBR })}`
  } else if (isYesterday(date)) {
    return `ontem, ${format(date, 'HH:mm', { locale: ptBR })}`
  }
  return `${format(date, 'dd/MM, HH:mm', { locale: ptBR })}`
}
