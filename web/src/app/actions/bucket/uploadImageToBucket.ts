'use server'

import { Client } from 'minio'

const endpoint = process.env.MINIO_ENDPOINT || ''
const port = Number(process.env.MINIO_PORT) || 9000
const accessKey = process.env.MINIO_ACCESSKEY || ''
const secretKey = process.env.MINIO_SECRETKEY || ''
const useSSL = !!process.env.MINIO_SSL

const minioClient = new Client({
  endPoint: endpoint,
  port,
  accessKey,
  secretKey,
  useSSL,
})

export const uploadImageToMinio = async (
  file: File,
): Promise<string | null> => {
  const bucketName = 'media'
  const objectName = `uploads/${Date.now()}-${file.name}`

  try {
    const arrayBuffer = await file.arrayBuffer()
    const buffer = Buffer.from(arrayBuffer)

    await minioClient.putObject(bucketName, objectName, buffer, buffer.length, {
      'Content-Type': file.type,
    })

    const presignedUrl = await minioClient.presignedUrl(
      'GET',
      bucketName,
      objectName,
      24 * 60 * 60,
    )
    return presignedUrl
  } catch (error) {
    console.error('Error uploading image:', error)
    return null
  }
}
