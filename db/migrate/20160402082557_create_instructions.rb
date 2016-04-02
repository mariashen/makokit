class CreateInstructions < ActiveRecord::Migration
  def change
    create_table :instructions do |t|
      t.integer :lesson_id
      t.text :text
      t.string :image_url
      t.string :video_url
      t.integer :display_position

      t.timestamps null: false
    end
  end
end
