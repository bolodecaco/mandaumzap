import { ServerActionError } from './createServerAction'

export function throwGenericError(err: unknown) {
  if (err instanceof Error) {
    throw new ServerActionError(err.message)
  }
  throw new ServerActionError('Um erro desconhecido aconteceu')
}
