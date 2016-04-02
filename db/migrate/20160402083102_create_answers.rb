class CreateAnswers < ActiveRecord::Migration
  def change
    create_table :answers do |t|
      t.integer :instruction_id
      t.string :text
      t.string :image_url

      t.timestamps null: false
    end
  end
end
