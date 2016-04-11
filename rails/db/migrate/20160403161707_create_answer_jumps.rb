class CreateAnswerJumps < ActiveRecord::Migration
  def change
    create_table :answer_jumps do |t|
      t.integer :answer_id
      t.integer :instruction_id

      t.timestamps null: false
    end
  end
end
