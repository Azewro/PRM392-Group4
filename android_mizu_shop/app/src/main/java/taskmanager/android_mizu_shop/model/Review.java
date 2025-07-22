package taskmanager.android_mizu_shop.model;

    public class Review {
        private int id;
        private String username;
        private int rating;
        private String comment;
        private String createdAt; // hoặc LocalDateTime nếu bạn xử lý được

        public Review(int id, String username, int rating, String comment, String createdAt) {
            this.id = id;
            this.username = username;
            this.rating = rating;
            this.comment = comment;
            this.createdAt = createdAt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }

