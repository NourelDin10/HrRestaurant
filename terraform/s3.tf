
 resource "aws_s3_bucket" "mys3" {
  bucket = "android-apk"

  tags = {
    Name        = "My bucket"
    Environment = "Dev"
  }
}
